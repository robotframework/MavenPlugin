class lib_with_arguments(object):

    def __init__(self, name):
        self._name = name

    def get_keyword_names(self):
        return [self._name]

    def run_keyword(self, name, args):
        print "Running keyword %s with arguments %s" % (name, args)